import { Component, computed, inject, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { JOB_LEVEL_LABELS, JOB_TYPE_LABELS, Job } from '../../../core/models/job.model';
import { Skill } from '../../../core/models/skill.model';
import { AuthService } from '../../../core/services/auth.service';
import { JobService } from '../../../core/services/job.service';
import { formatSalary } from '../../../shared/format';

@Component({
  selector: 'app-job-detail',
  imports: [RouterLink],
  templateUrl: './job-detail.html',
})
export class JobDetail {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly jobService = inject(JobService);
  protected readonly auth = inject(AuthService);

  protected readonly formatSalary = formatSalary;
  protected readonly typeLabels = JOB_TYPE_LABELS;
  protected readonly levelLabels = JOB_LEVEL_LABELS;

  protected readonly loading = signal(true);
  protected readonly notFound = signal(false);
  protected readonly job = signal<Job | null>(null);
  protected readonly skills = signal<Skill[]>([]);

  protected readonly applying = signal(false);
  protected readonly applySuccess = signal(false);
  protected readonly applyError = signal<string | null>(null);
  protected readonly deleting = signal(false);
  protected readonly actionError = signal<string | null>(null);

  // Heurística: sem o vínculo empresa->id exposto na API, comparamos pelo nome da empresa para exibir ações de dono.
  protected readonly isOwnerCompany = computed(
    () => this.auth.isCompany() && this.job()?.company === this.auth.currentUser()?.name,
  );

  constructor() {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    this.jobService.get(id).subscribe({
      next: (job) => {
        this.job.set(job);
        this.loading.set(false);
      },
      error: () => {
        this.notFound.set(true);
        this.loading.set(false);
      },
    });

    this.jobService.skillsOf(id).subscribe({
      next: (skills) => this.skills.set(skills),
      error: () => this.skills.set([]),
    });
  }

  apply(): void {
    const job = this.job();
    if (!job) return;

    this.applying.set(true);
    this.applyError.set(null);

    this.jobService.apply(job.id).subscribe({
      next: () => {
        this.applying.set(false);
        this.applySuccess.set(true);
      },
      error: () => {
        this.applying.set(false);
        this.applyError.set('Não foi possível enviar sua candidatura. Tente novamente.');
      },
    });
  }

  remove(): void {
    const job = this.job();
    if (!job || !confirm('Tem certeza que deseja excluir esta vaga?')) return;

    this.deleting.set(true);
    this.actionError.set(null);

    this.jobService.delete(job.id).subscribe({
      next: () => this.router.navigateByUrl('/vagas'),
      error: () => {
        this.deleting.set(false);
        this.actionError.set('Não foi possível excluir a vaga.');
      },
    });
  }
}
