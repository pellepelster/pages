import {
  Component,
  computed,
  effect,
  ElementRef,
  HostListener,
  inject,
  input,
  signal,
} from '@angular/core';
import { CloudComponent as CloudComponentModel, Group } from '../../models/config.model';
import { SelectionService } from '../../services/selection.service';
import { StaticBaseService } from '../../services/static-base.service';

function collectComponentNames(group: Group, names: Set<string>): void {
  for (const c of group.components ?? []) names.add(c.name);
  for (const g of group.groups ?? []) {
    if (g.type !== 'unknown') collectComponentNames(g, names);
  }
}

@Component({
  selector: 'app-cloud-component',
  templateUrl: './cloud-component.html',
  styleUrl: './cloud-component.scss',
  host: {
    '(click)': 'toggle()',
    '[class.selected]': 'isSelected()',
    '[class.color-0]': 'colorIndex() === 0',
    '[class.color-1]': 'colorIndex() === 1',
    '[class.color-2]': 'colorIndex() === 2',
    '[class.unknown]': "component().type === 'unknown'",
  },
})
export class CloudComponentComponent {
  component = input.required<CloudComponentModel>();
  group = input.required<Group>();
  externallyTurned = input(false);
  siblingGroups = input<Group[]>([]);
  parentGroup = input<Group | null>(null);

  private selectionService = inject(SelectionService);
  private staticBase = inject(StaticBaseService);
  private el = inject(ElementRef<HTMLElement>);
  private wasTurned = false;

  tooltipVisible = signal(false);
  tooltipStyle = signal<Record<string, string>>({});

  logoUrl = computed(() => {
    const logo = this.component().logo;
    if (!logo) return '';
    const base = this.staticBase.url();
    return base ? `${base}/${logo}` : logo;
  });

  isSelected = computed(() =>
    this.group().type === 'unknown'
      ? this.selectionService.isUnknownGroupSelected(this.group())()
      : this.selectionService.isSelected(this.component().name)(),
  );

  colorIndex = computed(() =>
    this.group().type === 'unknown'
      ? this.selectionService.getGroupColorIndex(this.group())()
      : this.selectionService.getColorIndex(this.component().name)(),
  );

  effectiveInfo = computed(() => {
    const raw = this.component().info ?? this.group().info ?? null;
    if (!raw) return null;
    return raw
      .split(/\n{2,}/)
      .map((para) => para.replace(/\n/g, ' ').trim())
      .join('\n');
  });

  isTurned = computed(() => {
    if (this.component().type === 'unknown') return false;
    if (this.externallyTurned()) return true;
    const unknownInGroup = (this.group().components ?? []).find((c) => c.type === 'unknown');
    if (!unknownInGroup) return false;
    return this.selectionService.isSelectedComponent(unknownInGroup)();
  });

  constructor() {
    effect(() => {
      const turned = this.isTurned();
      const host = this.el.nativeElement;
      if (turned === this.wasTurned) return;
      if (turned) {
        host.classList.remove('unturning');
        host.classList.add('turned');
      } else {
        host.classList.remove('turned');
        host.classList.add('unturning');
        setTimeout(() => host.classList.remove('unturning'), 500);
      }
      this.wasTurned = turned;
    });
  }

  @HostListener('mouseenter')
  onMouseEnter(): void {
    if (!this.effectiveInfo()) return;
    this.tooltipStyle.set({ left: '-9999px', top: '-9999px' });
    this.tooltipVisible.set(true);
    setTimeout(() => this.repositionTooltip(), 0);
  }

  @HostListener('mouseleave')
  onMouseLeave(): void {
    this.tooltipVisible.set(false);
  }

  private repositionTooltip(): void {
    if (!this.tooltipVisible()) return;
    const host = this.el.nativeElement as HTMLElement;
    const tooltip = host.querySelector<HTMLElement>('.tooltip');
    if (!tooltip) return;

    const hostRect = host.getBoundingClientRect();
    const tw = tooltip.offsetWidth;
    const th = tooltip.offsetHeight;
    const gap = 10;
    const margin = 8;

    let left = hostRect.right + gap;
    if (left + tw > window.innerWidth - margin) {
      left = hostRect.left - tw - gap;
    }
    left = Math.max(margin, left);

    let top = hostRect.top + hostRect.height / 2 - th / 2;
    top = Math.max(margin, Math.min(top, window.innerHeight - th - margin));

    this.tooltipStyle.set({ left: `${left}px`, top: `${top}px` });
  }

  toggle(): void {
    if (this.group().type === 'unknown') {
      const siblingNames = new Set<string>();
      for (const g of this.siblingGroups()) {
        if (g === this.group() || g.type === 'unknown') continue;
        collectComponentNames(g, siblingNames);
      }
      this.selectionService.toggleUnknownGroup(this.group(), this.parentGroup(), siblingNames);
    } else {
      this.selectionService.toggle(this.component(), this.group());
    }
  }
}
