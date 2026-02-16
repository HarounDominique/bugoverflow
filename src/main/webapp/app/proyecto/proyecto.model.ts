export class ProyectoDTO {

  constructor(data:Partial<ProyectoDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  titulo?: string|null;
  descripcion?: string|null;
  urlRepo?: string|null;
  categoria?: string|null;
  estado?: string|null;
  autor?: number|null;
  skills?: number[]|null;

}
