export class UsuarioPreferenciaDTO {

  constructor(data:Partial<UsuarioPreferenciaDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  userId?: number|null;
  rol?: string|null;
  nivelInteres?: number|null;
  usuario?: number|null;

}
