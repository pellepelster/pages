import { Injectable, signal } from '@angular/core';

@Injectable()
export class StaticBaseService {
  readonly url = signal('');
}
