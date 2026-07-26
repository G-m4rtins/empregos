import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, expand, map, reduce, takeWhile } from 'rxjs';
import { environment } from '../../../environments/environment';
import { HalPage, PageInfo, itemsFromHal } from '../models/page.model';
import { Skill, SkillRequest } from '../models/skill.model';

export interface SkillPage {
  items: Skill[];
  page: PageInfo;
}

@Injectable({ providedIn: 'root' })
export class SkillService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = `${environment.apiUrl}/skills`;

  list(page = 1, size = 10): Observable<SkillPage> {
    return this.http
      .get<HalPage<Skill>>(this.baseUrl, { params: { pagina: page, tamanho: size } })
      .pipe(
        map((hal) => ({
          items: itemsFromHal(hal),
          page: hal.page ?? { size, totalElements: 0, totalPages: 0, number: page - 1 },
        })),
      );
  }

  /** Skills são paginadas no backend (máx. 10/página); aqui buscamos todas as páginas para os seletores de skills. */
  listAll(): Observable<Skill[]> {
    return this.list(1, 10).pipe(
      expand((current) =>
        current.page.number + 1 < current.page.totalPages
          ? this.list(current.page.number + 2, 10)
          : [],
      ),
      takeWhile((current) => current.items.length > 0, true),
      reduce<SkillPage, Skill[]>((acc, current) => [...acc, ...current.items], []),
    );
  }

  get(id: number): Observable<Skill> {
    return this.http.get<Skill>(`${this.baseUrl}/${id}`);
  }

  create(payload: SkillRequest): Observable<Skill> {
    return this.http.post<Skill>(this.baseUrl, payload);
  }

  update(id: number, payload: SkillRequest): Observable<Skill> {
    return this.http.put<Skill>(`${this.baseUrl}/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
