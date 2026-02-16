export class UsuarioSkillDTO {

  constructor(data:Partial<UsuarioSkillDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  userId?: number|null;
  skillId?: number|null;
  nivelInteres?: number|null;
  usuario?: number|null;
  skill?: number|null;

}
