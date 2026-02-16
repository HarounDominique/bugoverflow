import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { PerfilUsuarioListComponent } from './perfil-usuario/perfil-usuario-list.component';
import { PerfilUsuarioAddComponent } from './perfil-usuario/perfil-usuario-add.component';
import { PerfilUsuarioEditComponent } from './perfil-usuario/perfil-usuario-edit.component';
import { SkillListComponent } from './skill/skill-list.component';
import { SkillAddComponent } from './skill/skill-add.component';
import { SkillEditComponent } from './skill/skill-edit.component';
import { ProyectoListComponent } from './proyecto/proyecto-list.component';
import { ProyectoAddComponent } from './proyecto/proyecto-add.component';
import { ProyectoEditComponent } from './proyecto/proyecto-edit.component';
import { CandidaturaListComponent } from './candidatura/candidatura-list.component';
import { CandidaturaAddComponent } from './candidatura/candidatura-add.component';
import { CandidaturaEditComponent } from './candidatura/candidatura-edit.component';
import { UsuarioSkillListComponent } from './usuario-skill/usuario-skill-list.component';
import { UsuarioSkillAddComponent } from './usuario-skill/usuario-skill-add.component';
import { UsuarioSkillEditComponent } from './usuario-skill/usuario-skill-edit.component';
import { UsuarioPreferenciaListComponent } from './usuario-preferencia/usuario-preferencia-list.component';
import { UsuarioPreferenciaAddComponent } from './usuario-preferencia/usuario-preferencia-add.component';
import { UsuarioPreferenciaEditComponent } from './usuario-preferencia/usuario-preferencia-edit.component';
import { UsuarioListComponent } from './usuario/usuario-list.component';
import { UsuarioAddComponent } from './usuario/usuario-add.component';
import { UsuarioEditComponent } from './usuario/usuario-edit.component';
import { ErrorComponent } from './error/error.component';


export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    title: $localize`:@@home.index.headline:Welcome to your new app!`
  },
  {
    path: 'perfilUsuarios',
    component: PerfilUsuarioListComponent,
    title: $localize`:@@perfilUsuario.list.headline:Perfil Usuarios`
  },
  {
    path: 'perfilUsuarios/add',
    component: PerfilUsuarioAddComponent,
    title: $localize`:@@perfilUsuario.add.headline:Add Perfil Usuario`
  },
  {
    path: 'perfilUsuarios/edit/:id',
    component: PerfilUsuarioEditComponent,
    title: $localize`:@@perfilUsuario.edit.headline:Edit Perfil Usuario`
  },
  {
    path: 'skills',
    component: SkillListComponent,
    title: $localize`:@@skill.list.headline:Skills`
  },
  {
    path: 'skills/add',
    component: SkillAddComponent,
    title: $localize`:@@skill.add.headline:Add Skill`
  },
  {
    path: 'skills/edit/:id',
    component: SkillEditComponent,
    title: $localize`:@@skill.edit.headline:Edit Skill`
  },
  {
    path: 'proyectos',
    component: ProyectoListComponent,
    title: $localize`:@@proyecto.list.headline:Proyectoes`
  },
  {
    path: 'proyectos/add',
    component: ProyectoAddComponent,
    title: $localize`:@@proyecto.add.headline:Add Proyecto`
  },
  {
    path: 'proyectos/edit/:id',
    component: ProyectoEditComponent,
    title: $localize`:@@proyecto.edit.headline:Edit Proyecto`
  },
  {
    path: 'candidaturas',
    component: CandidaturaListComponent,
    title: $localize`:@@candidatura.list.headline:Candidaturas`
  },
  {
    path: 'candidaturas/add',
    component: CandidaturaAddComponent,
    title: $localize`:@@candidatura.add.headline:Add Candidatura`
  },
  {
    path: 'candidaturas/edit/:id',
    component: CandidaturaEditComponent,
    title: $localize`:@@candidatura.edit.headline:Edit Candidatura`
  },
  {
    path: 'usuarioSkills',
    component: UsuarioSkillListComponent,
    title: $localize`:@@usuarioSkill.list.headline:Usuario Skills`
  },
  {
    path: 'usuarioSkills/add',
    component: UsuarioSkillAddComponent,
    title: $localize`:@@usuarioSkill.add.headline:Add Usuario Skill`
  },
  {
    path: 'usuarioSkills/edit/:id',
    component: UsuarioSkillEditComponent,
    title: $localize`:@@usuarioSkill.edit.headline:Edit Usuario Skill`
  },
  {
    path: 'usuarioPreferencias',
    component: UsuarioPreferenciaListComponent,
    title: $localize`:@@usuarioPreferencia.list.headline:Usuario Preferencias`
  },
  {
    path: 'usuarioPreferencias/add',
    component: UsuarioPreferenciaAddComponent,
    title: $localize`:@@usuarioPreferencia.add.headline:Add Usuario Preferencia`
  },
  {
    path: 'usuarioPreferencias/edit/:id',
    component: UsuarioPreferenciaEditComponent,
    title: $localize`:@@usuarioPreferencia.edit.headline:Edit Usuario Preferencia`
  },
  {
    path: 'usuarios',
    component: UsuarioListComponent,
    title: $localize`:@@usuario.list.headline:Usuarios`
  },
  {
    path: 'usuarios/add',
    component: UsuarioAddComponent,
    title: $localize`:@@usuario.add.headline:Add Usuario`
  },
  {
    path: 'usuarios/edit/:id',
    component: UsuarioEditComponent,
    title: $localize`:@@usuario.edit.headline:Edit Usuario`
  },
  {
    path: 'error',
    component: ErrorComponent,
    title: $localize`:@@error.page.headline:Error`
  },
  {
    path: '**',
    component: ErrorComponent,
    title: $localize`:@@notFound.headline:Page not found`
  }
];
