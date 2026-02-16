export class SkillDTO {

  constructor(data:Partial<SkillDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  nombre?: string|null;
  categoria?: string|null;

}
