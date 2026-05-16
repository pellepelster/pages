import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as jsyaml from 'js-yaml';
import { Config } from '../models/config.model';

@Injectable({ providedIn: 'root' })
export class ConfigService {
  private http = inject(HttpClient);

  getConfig(url: string): Observable<Config> {
    return this.http
      .get(url, { responseType: 'text' })
      .pipe(map((text) => jsyaml.load(text) as Config));
  }
}
