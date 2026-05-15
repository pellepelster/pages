export interface CloudComponent {
  name: string;
  type?: string;
  logo?: string;
  selected?: string;
  categories?: string[];
  info?: string;
}

export interface Group {
  name: string;
  type?: string;
  direction: 'horizontal' | 'vertical';
  selection_mode?: 'multi' | 'radio';
  info?: string;
  selected?: string;
  groups?: Group[];
  components?: CloudComponent[];
}

export interface Config {
  groups: Group[];
}
