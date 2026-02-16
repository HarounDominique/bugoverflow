export class UsuarioDTO {

  constructor(data:Partial<UsuarioDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  login?: string|null;
  email?: string|null;
  password?: string|null;
  nombre?: string|null;
  apellido?: string|null;
  activo?: boolean|null;

}
