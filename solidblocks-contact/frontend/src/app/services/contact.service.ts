import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ContactService {
  private http = inject(HttpClient);

  submit(baseUrl: string, email: string, components: string[]): Observable<unknown> {
    return this.http.post(`${baseUrl}/api/home/contact`, { email, components });
  }
}
