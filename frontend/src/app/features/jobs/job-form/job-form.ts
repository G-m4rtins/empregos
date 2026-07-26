import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import { JOB_LEVEL_LABELS, JOB_TYPE_LABELS, JobLevel, JobType } from '../../../core/models/job.model';
import { Skill } from '../../../core/models/skill.model';
import { JobService } from '../../../core/services/job.service';
import { SkillService } from '../../../core/services/skill.service';

@Component({
  selector: 'app-job-form',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './job-form.html',
})
export class JobForm {
  private readonly fb = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly jobService = inject(JobService);
  private readonly skillService = inject(SkillService);

  protected readonly typeOptions = Object.entries(JOB_TYPE_LABELS) as [JobType, string][];
  protected readonly levelOptions = Object.entries(JOB_LEVEL_LABELS) as [JobLevel, string][];

  protected readonly jobId = Number(this.route.snapshot.paramMap.get('id')) || null;
  protected readonly isEdit = this.jobId !== null;

  protected readonly loading = signal(true);
  protected readonly saving = signal(false);
  protected readonly errorMessage = signal<string | null>(null);
  protected readonly availableSkills = signal<Skill[]>([]);
  protected readonly selectedSkillIds = signal<Set<number>>(new Set());

  protected readonly form = this.fb.nonNullable.group({
    title: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
    description: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(255)]],
    location: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
    jobType: ['FULL_TIME' as JobType, Validators.required],
    jobLevel: ['JUNIOR' as JobLevel, Validators.required],
    salary: [0, [Validators.required, Validators.min(0)]],
  });

  constructor() {
    const skills$ = this.skillService.listAll();

    if (this.isEdit && this.jobId) {
      forkJoin({
        job: this.jobService.get(this.jobId),
        jobSkills: this.jobService.skillsOf(this.jobId),
        skills: skills$,
      }).subscribe({
        next: ({ job, jobSkills, skills }) => {
          this.availableSkills.set(skills);
          this.selectedSkillIds.set(new Set(jobSkills.map((skill) => skill.id)));
          this.form.patchValue({
            title: job.title,
            description: job.description,
            location: job.location,
            jobType: job.jobType,
            jobLevel: job.jobLevel,
            salary: job.salary,
          });
          this.loading.set(false);
        },
        error: () => {
          this.errorMessage.set('Não foi possível carregar a vaga para edição.');
          this.loading.set(false);
        },
      });
    } else {
      skills$.subscribe({
        next: (skills) => {
          this.availableSkills.set(skills);
          this.loading.set(false);
        },
        error: () => {
          this.errorMessage.set('Não foi possível carregar as skills disponíveis.');
          this.loading.set(false);
        },
      });
    }
  }

  toggleSkill(id: number): void {
    const next = new Set(this.selectedSkillIds());
    if (next.has(id)) {
      next.delete(id);
    } else {
      next.add(id);
    }
    this.selectedSkillIds.set(next);
  }

  submit(): void {
    if (this.form.invalid || this.selectedSkillIds().size === 0) {
      this.form.markAllAsTouched();
      if (this.selectedSkillIds().size === 0) {
        this.errorMessage.set('Selecione ao menos uma skill.');
      }
      return;
    }

    this.saving.set(true);
    this.errorMessage.set(null);

    const payload = { ...this.form.getRawValue(), skills: Array.from(this.selectedSkillIds()) };
    const request =
      this.isEdit && this.jobId
        ? this.jobService.update(this.jobId, payload)
        : this.jobService.create(payload);

    request.subscribe({
      next: (job) => this.router.navigate(['/vagas', job.id]),
      error: () => {
        this.saving.set(false);
        this.errorMessage.set('Não foi possível salvar a vaga. Verifique os dados e tente novamente.');
      },
    });
  }
}
