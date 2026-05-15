import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ContactService {
  private http = inject(HttpClient);

  submit(email: string, components: string[]): Observable<unknown> {
    return this.http.post('/api/home/contact', { email, components });
  }
}
