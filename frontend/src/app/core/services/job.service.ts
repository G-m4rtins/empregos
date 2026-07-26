import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Job, JobRequest } from '../models/job.model';
import { Skill } from '../models/skill.model';

@Injectable({ providedIn: 'root' })
export class JobService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/jobs`;

  list(): Observable<Job[]> {
    return this.http.get<Job[]>(this.baseUrl);
  }

  get(id: number): Observable<Job> {
    return this.http.get<Job>(`${this.baseUrl}/${id}`);
  }

  create(payload: JobRequest): Observable<Job> {
    return this.http.post<Job>(this.baseUrl, payload);
  }

  update(id: number, payload: JobRequest): Observable<Job> {
    return this.http.put<Job>(`${this.baseUrl}/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  skillsOf(id: number): Observable<Skill[]> {
    return this.http.get<Skill[]>(`${this.baseUrl}/${id}/skills`);
  }

  apply(id: number): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/${id}/apply`, {});
  }
}
