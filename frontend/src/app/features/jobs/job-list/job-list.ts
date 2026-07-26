import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { JOB_LEVEL_LABELS, JOB_TYPE_LABELS, Job, JobLevel, JobType } from '../../../core/models/job.model';
import { JobService } from '../../../core/services/job.service';
import { formatSalary } from '../../../shared/format';

@Component({
  selector: 'app-job-list',
  imports: [FormsModule, RouterLink],
  templateUrl: './job-list.html',
})
export class JobList {
  private readonly jobService = inject(JobService);

  protected readonly formatSalary = formatSalary;
  protected readonly typeLabels = JOB_TYPE_LABELS;
  protected readonly levelLabels = JOB_LEVEL_LABELS;
  protected readonly typeOptions = Object.entries(JOB_TYPE_LABELS) as [JobType, string][];
  protected readonly levelOptions = Object.entries(JOB_LEVEL_LABELS) as [JobLevel, string][];

  protected readonly loading = signal(true);
  protected readonly errorMessage = signal<string | null>(null);
  protected readonly jobs = signal<Job[]>([]);

  protected readonly search = signal('');
  protected readonly typeFilter = signal<JobType | ''>('');
  protected readonly levelFilter = signal<JobLevel | ''>('');

  protected readonly filteredJobs = computed(() => {
    const term = this.search().trim().toLowerCase();
    const type = this.typeFilter();
    const level = this.levelFilter();

    return this.jobs().filter((job) => {
      const matchesTerm =
        !term ||
        job.title.toLowerCase().includes(term) ||
        job.company.toLowerCase().includes(term) ||
        job.location.toLowerCase().includes(term);
      const matchesType = !type || job.jobType === type;
      const matchesLevel = !level || job.jobLevel === level;
      return matchesTerm && matchesType && matchesLevel;
    });
  });

  constructor() {
    this.jobService.list().subscribe({
      next: (jobs) => {
        this.jobs.set(jobs);
        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set('Não foi possível carregar as vagas. Tente novamente mais tarde.');
        this.loading.set(false);
      },
    });
  }
}
