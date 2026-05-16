import { Component, computed, inject, Input, OnInit, signal } from '@angular/core';
import { ConfigService } from './services/config.service';
import { ContactService } from './services/contact.service';
import { SelectionService } from './services/selection.service';
import { StaticBaseService } from './services/static-base.service';
import { Config, Group } from './models/config.model';
import { GroupComponent } from './components/group/group';

@Component({
  selector: 'app-root',
  imports: [GroupComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss',
  providers: [StaticBaseService],
})
export class App implements OnInit {
  @Input() baseUrl = '';
  @Input() configUrl = '/api/home/config.yml';

  private configService = inject(ConfigService);
  private contactService = inject(ContactService);
  protected selectionService = inject(SelectionService);
  private staticBase = inject(StaticBaseService);

  config = signal<Config | null>(null);
  error = signal<string | null>(null);
  email = signal('');
  submitStatus = signal<'idle' | 'sending' | 'sent' | 'error'>('idle');

  selectedByGroup = computed(() => {
    const map = new Map<
      Group,
      { component: { name: string; type?: string; selected?: string } }[]
    >();
    for (const entry of this.selectionService.selected()) {
      if (!map.has(entry.group)) map.set(entry.group, []);
      map.get(entry.group)!.push(entry);
    }

    const result: { groupName: string | null; items: string[] }[] = [...map.entries()].map(
      ([group, entries]) => {
        if (group.selected) {
          const unknown = entries.find((e) => e.component.type === 'unknown');
          if (unknown) {
            return {
              groupName: group.name,
              items: [unknown.component.selected ?? unknown.component.name],
            };
          }
          const names = entries.map((e) => e.component.name);
          const joined =
            names.length === 1
              ? names[0]
              : names.slice(0, -1).join(', ') + ' and ' + names[names.length - 1];
          return { groupName: group.name, items: [`${group.selected} ${joined}`] };
        }
        return {
          groupName: group.name,
          items: entries.map((e) => e.component.selected ?? e.component.name),
        };
      },
    );

    for (const entry of this.selectionService.selectedUnknownGroups()) {
      const text = entry.group.selected ?? entry.group.name;
      if (text) result.push({ groupName: entry.parentGroup?.name ?? null, items: [text] });
    }

    return result;
  });

  ngOnInit(): void {
    this.staticBase.url.set(this.baseUrl ? `${this.baseUrl}/static` : '');
    this.configService.getConfig(`${this.baseUrl}${this.configUrl}`).subscribe({
      next: (cfg) => this.config.set(cfg),
      error: (err) => this.error.set(String(err)),
    });
  }

  submitContact(): void {
    const email = this.email();
    const selected = this.selectionService.selected();
    const components = selected.length > 0 ? selected.map((e) => e.component.name) : ['anything'];
    this.submitStatus.set('sending');
    this.contactService.submit(this.baseUrl, email, components).subscribe({
      next: () => this.submitStatus.set('sent'),
      error: () => this.submitStatus.set('error'),
    });
  }
}
