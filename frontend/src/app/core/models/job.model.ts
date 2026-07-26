export type JobType = 'FULL_TIME' | 'PART_TIME' | 'FREELANCE' | 'INTERSHIP' | 'TEMPORARY';
export type JobLevel = 'JUNIOR' | 'MID_LEVEL' | 'SENIOR';

export const JOB_TYPE_LABELS: Record<JobType, string> = {
  FULL_TIME: 'Tempo integral',
  PART_TIME: 'Meio período',
  FREELANCE: 'Freelance',
  INTERSHIP: 'Estágio',
  TEMPORARY: 'Temporário',
};

export const JOB_LEVEL_LABELS: Record<JobLevel, string> = {
  JUNIOR: 'Júnior',
  MID_LEVEL: 'Pleno',
  SENIOR: 'Sênior',
};

export interface Job {
  id: number;
  title: string;
  description: string;
  company: string;
  location: string;
  jobType: JobType;
  jobLevel: JobLevel;
  salary: number;
}

export interface JobRequest {
  title: string;
  description: string;
  location: string;
  jobType: JobType;
  jobLevel: JobLevel;
  salary: number;
  skills: number[];
}
