import { ISkill, NewSkill } from './skill.model';

export const sampleWithRequiredData: ISkill = {
  id: 18455,
  nombre: 'mmm',
};

export const sampleWithPartialData: ISkill = {
  id: 4373,
  nombre: 'bonnet',
  descripcion: 'whistle yesterday',
};

export const sampleWithFullData: ISkill = {
  id: 24296,
  nombre: 'as scheme',
  descripcion: 'rosemary',
};

export const sampleWithNewData: NewSkill = {
  nombre: 'those throbbing coast',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
