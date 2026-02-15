import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'bugoverflowApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'proyecto',
    data: { pageTitle: 'bugoverflowApp.proyecto.home.title' },
    loadChildren: () => import('./proyecto/proyecto.routes'),
  },
  {
    path: 'perfil-usuario',
    data: { pageTitle: 'bugoverflowApp.perfilUsuario.home.title' },
    loadChildren: () => import('./perfil-usuario/perfil-usuario.routes'),
  },
  {
    path: 'skill',
    data: { pageTitle: 'bugoverflowApp.skill.home.title' },
    loadChildren: () => import('./skill/skill.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
