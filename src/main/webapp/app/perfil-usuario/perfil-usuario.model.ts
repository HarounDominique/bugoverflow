export class PerfilUsuarioDTO {

  constructor(data:Partial<PerfilUsuarioDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  nombreVisible?: string|null;
  bio?: string|null;
  github?: string|null;
  webPersonal?: string|null;
  avatarUrl?: string|null;
  usuario?: number|null;
  skills?: number[]|null;

}
