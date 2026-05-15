import { Component, computed, effect, ElementRef, inject, input } from '@angular/core';
import { CloudComponent as CloudComponentModel, Group } from '../../models/config.model';
import { SelectionService } from '../../services/selection.service';

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
  private el = inject(ElementRef<HTMLElement>);
  private wasTurned = false;

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

  effectiveInfo = computed(() => this.component().info ?? this.group().info ?? null);

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
