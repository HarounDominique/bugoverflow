import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SkillResolve from './route/skill-routing-resolve.service';

const skillRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/skill.component').then(m => m.SkillComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/skill-detail.component').then(m => m.SkillDetailComponent),
    resolve: {
      skill: SkillResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/skill-update.component').then(m => m.SkillUpdateComponent),
    resolve: {
      skill: SkillResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/skill-update.component').then(m => m.SkillUpdateComponent),
    resolve: {
      skill: SkillResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default skillRoute;
