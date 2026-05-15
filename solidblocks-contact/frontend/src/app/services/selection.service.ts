import { computed, Injectable, signal } from '@angular/core';
import { CloudComponent, Group } from '../models/config.model';

interface SelectedEntry {
  component: CloudComponent;
  group: Group;
}

interface SelectedUnknownGroupEntry {
  group: Group;
  parentGroup: Group | null;
}

@Injectable({ providedIn: 'root' })
export class SelectionService {
  private _selected = signal<SelectedEntry[]>([]);
  private _colorAssignments = signal<Map<string, number>>(new Map());
  private _selectedUnknownGroups = signal<SelectedUnknownGroupEntry[]>([]);
  private _groupColorAssignments = signal<Map<Group, number>>(new Map());
  private _colorCounter = 0;

  readonly selected = this._selected.asReadonly();
  readonly selectedUnknownGroups = this._selectedUnknownGroups.asReadonly();

  isSelected(name: string) {
    return computed(() => this._selected().some((e) => e.component.name === name));
  }

  isSelectedComponent(component: CloudComponent) {
    return computed(() => this._selected().some((e) => e.component === component));
  }

  getColorIndex(name: string) {
    return computed(() => this._colorAssignments().get(name) ?? -1);
  }

  isUnknownGroupSelected(group: Group) {
    return computed(() => this._selectedUnknownGroups().some((e) => e.group === group));
  }

  getGroupColorIndex(group: Group) {
    return computed(() => this._groupColorAssignments().get(group) ?? -1);
  }

  toggleUnknownGroup(
    group: Group,
    parentGroup: Group | null = null,
    siblingComponentNames: Set<string> = new Set(),
  ): void {
    const current = this._selectedUnknownGroups();
    const alreadySelected = current.some((e) => e.group === group);

    this._selectedUnknownGroups.set(
      alreadySelected
        ? current.filter((e) => e.group !== group)
        : [...current, { group, parentGroup }],
    );

    this._groupColorAssignments.update((map) => {
      const updated = new Map(map);
      if (alreadySelected) {
        updated.delete(group);
      } else {
        updated.set(group, this._colorCounter % 3);
        this._colorCounter++;
      }
      return updated;
    });

    if (!alreadySelected && siblingComponentNames.size > 0) {
      const removed = this._selected().filter((e) => siblingComponentNames.has(e.component.name));
      if (removed.length > 0) {
        this._selected.update((sel) =>
          sel.filter((e) => !siblingComponentNames.has(e.component.name)),
        );
        this._colorAssignments.update((map) => {
          const updated = new Map(map);
          for (const e of removed) updated.delete(e.component.name);
          return updated;
        });
      }
    }
  }

  toggle(component: CloudComponent, group: Group): void {
    const isRadio = group.selection_mode === 'radio';
    const isUnknown = component.type === 'unknown';
    const groupNames = new Set((group.components ?? []).map((c) => c.name));
    const current = this._selected();
    const alreadySelected = current.some((e) => e.component.name === component.name);
    const withoutGroup = current.filter((e) => !groupNames.has(e.component.name));

    let next: SelectedEntry[];
    if (isRadio || isUnknown) {
      next = alreadySelected ? withoutGroup : [...withoutGroup, { component, group }];
    } else {
      const groupUnknown = (group.components ?? []).find((c) => c.type === 'unknown');
      const unknownActiveInGroup =
        !!groupUnknown && current.some((e) => e.component === groupUnknown);
      const base = unknownActiveInGroup
        ? current.filter((e) => e.component !== groupUnknown)
        : current;
      next = alreadySelected
        ? base.filter((e) => e.component.name !== component.name)
        : [...base, { component, group }];
    }

    const oldNames = new Set(current.map((e) => e.component.name));
    const newNames = new Set(next.map((e) => e.component.name));
    const added = next.filter((e) => !oldNames.has(e.component.name));
    const removed = current.filter((e) => !newNames.has(e.component.name));

    this._selected.set(next);

    this._colorAssignments.update((map) => {
      const updated = new Map(map);
      for (const e of removed) updated.delete(e.component.name);
      for (const e of added) {
        updated.set(e.component.name, this._colorCounter % 3);
        this._colorCounter++;
      }
      return updated;
    });
  }
}
