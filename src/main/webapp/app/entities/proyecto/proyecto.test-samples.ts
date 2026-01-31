import { IProyecto, NewProyecto } from './proyecto.model';

export const sampleWithRequiredData: IProyecto = {
  id: 19316,
  titulo: 'down',
  descripcion: 'worth since mobilise',
  categoria: 'IAML',
  estado: 'CERRADO',
};

export const sampleWithPartialData: IProyecto = {
  id: 1419,
  titulo: 'merrily exploration',
  descripcion: 'which probe',
  categoria: 'IAML',
  estado: 'CERRADO',
};

export const sampleWithFullData: IProyecto = {
  id: 17840,
  titulo: 'pointed daintily',
  descripcion: 'untidy profitable',
  urlrepo: 'what embed',
  categoria: 'WEB',
  estado: 'CERRADO',
};

export const sampleWithNewData: NewProyecto = {
  titulo: 'which unconscious thorny',
  descripcion: 'next pertinent',
  categoria: 'MOVIL',
  estado: 'CERRADO',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
