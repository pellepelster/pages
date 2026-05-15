import { Component, computed, inject, input } from '@angular/core';
import { Group, CloudComponent as CloudComponentModel } from '../../models/config.model';
import { CloudComponentComponent } from '../cloud-component/cloud-component';
import { SelectionService } from '../../services/selection.service';

@Component({
  selector: 'app-group',
  imports: [GroupComponent, CloudComponentComponent],
  templateUrl: './group.html',
  styleUrl: './group.scss',
  host: {
    '[class.vertical]': "group().direction === 'vertical'",
    '[class.unknown]': "group().type === 'unknown'",
  },
})
export class GroupComponent {
  group = input.required<Group>();
  turningAll = input(false);
  siblingGroups = input<Group[]>([]);
  parentGroup = input<Group | null>(null);

  private selectionService = inject(SelectionService);

  readonly syntheticUnknownComponent: CloudComponentModel = {
    name: '',
    type: 'unknown',
    logo: 'logos/person-circle-question.svg',
  };

  anyUnknownChildSelected = computed(() =>
    (this.group().groups ?? [])
      .filter((g) => g.type === 'unknown')
      .some((g) => this.selectionService.isUnknownGroupSelected(g)()),
  );
}
