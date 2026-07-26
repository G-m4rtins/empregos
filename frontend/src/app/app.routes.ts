import { Routes } from '@angular/router';
import { authGuard, guestGuard, roleGuard } from './core/guards/auth.guard';
import { Shell } from './layout/shell/shell';

export const routes: Routes = [
  {
    path: '',
    component: Shell,
    children: [
      { path: '', redirectTo: 'vagas', pathMatch: 'full' },
      {
        path: 'login',
        canActivate: [guestGuard],
        loadComponent: () => import('./features/auth/login/login').then((m) => m.Login),
      },
      {
        path: 'cadastro',
        canActivate: [guestGuard],
        loadComponent: () => import('./features/auth/register/register').then((m) => m.Register),
      },
      {
        path: 'vagas',
        canActivate: [authGuard],
        loadComponent: () => import('./features/jobs/job-list/job-list').then((m) => m.JobList),
      },
      {
        path: 'vagas/nova',
        canActivate: [roleGuard('COMPANY')],
        loadComponent: () => import('./features/jobs/job-form/job-form').then((m) => m.JobForm),
      },
      {
        path: 'vagas/:id',
        canActivate: [authGuard],
        loadComponent: () => import('./features/jobs/job-detail/job-detail').then((m) => m.JobDetail),
      },
      {
        path: 'vagas/:id/editar',
        canActivate: [roleGuard('COMPANY')],
        loadComponent: () => import('./features/jobs/job-form/job-form').then((m) => m.JobForm),
      },
      {
        path: 'skills',
        canActivate: [roleGuard('COMPANY')],
        loadComponent: () => import('./features/skills/skill-list/skill-list').then((m) => m.SkillList),
      },
      { path: '**', redirectTo: 'vagas' },
    ],
  },
];
