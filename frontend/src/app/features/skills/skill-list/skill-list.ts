import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Skill } from '../../../core/models/skill.model';
import { SkillService } from '../../../core/services/skill.service';
import { PageInfo } from '../../../core/models/page.model';

@Component({
  selector: 'app-skill-list',
  imports: [FormsModule],
  templateUrl: './skill-list.html',
})
export class SkillList {
  private readonly skillService = inject(SkillService);
  private readonly pageSize = 10;

  protected readonly loading = signal(true);
  protected readonly errorMessage = signal<string | null>(null);
  protected readonly skills = signal<Skill[]>([]);
  protected readonly pageInfo = signal<PageInfo | null>(null);

  protected readonly newSkillName = signal('');
  protected readonly creating = signal(false);
  protected readonly createError = signal<string | null>(null);

  protected readonly editingId = signal<number | null>(null);
  protected readonly editingName = signal('');
  protected readonly savingEditId = signal<number | null>(null);
  protected readonly deletingId = signal<number | null>(null);

  constructor() {
    this.loadPage(1);
  }

  loadPage(page: number): void {
    this.loading.set(true);
    this.errorMessage.set(null);

    this.skillService.list(page, this.pageSize).subscribe({
      next: ({ items, page: pageInfo }) => {
        this.skills.set(items);
        this.pageInfo.set(pageInfo);
        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set('Não foi possível carregar as skills.');
        this.loading.set(false);
      },
    });
  }

  createSkill(): void {
    const name = this.newSkillName().trim();
    if (!name) return;

    this.creating.set(true);
    this.createError.set(null);

    this.skillService.create({ name }).subscribe({
      next: () => {
        this.creating.set(false);
        this.newSkillName.set('');
        this.loadPage(1);
      },
      error: (err) => {
        this.creating.set(false);
        this.createError.set(
          err?.status === 400 ? 'Essa skill já existe ou é inválida.' : 'Não foi possível criar a skill.',
        );
      },
    });
  }

  startEdit(skill: Skill): void {
    this.editingId.set(skill.id);
    this.editingName.set(skill.name);
  }

  cancelEdit(): void {
    this.editingId.set(null);
  }

  saveEdit(id: number): void {
    const name = this.editingName().trim();
    if (!name) return;

    this.savingEditId.set(id);

    this.skillService.update(id, { name }).subscribe({
      next: (updated) => {
        this.skills.update((list) => list.map((skill) => (skill.id === id ? updated : skill)));
        this.savingEditId.set(null);
        this.editingId.set(null);
      },
      error: () => {
        this.savingEditId.set(null);
        this.errorMessage.set('Não foi possível atualizar a skill.');
      },
    });
  }

  removeSkill(id: number): void {
    if (!confirm('Remover esta skill?')) return;

    this.deletingId.set(id);

    this.skillService.delete(id).subscribe({
      next: () => {
        this.deletingId.set(null);
        const page = this.pageInfo();
        const isLastItemOnPage = this.skills().length === 1 && page && page.number > 0;
        this.loadPage(isLastItemOnPage ? page!.number : (page?.number ?? 0) + 1);
      },
      error: () => {
        this.deletingId.set(null);
        this.errorMessage.set('Não foi possível remover a skill.');
      },
    });
  }
}
