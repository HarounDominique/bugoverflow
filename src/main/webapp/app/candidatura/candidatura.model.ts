export class CandidaturaDTO {

  constructor(data:Partial<CandidaturaDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  fecha?: string|null;
  estado?: string|null;
  usuario?: number|null;
  proyecto?: number|null;

}
