import { IPerfilUsuario, NewPerfilUsuario } from './perfil-usuario.model';

export const sampleWithRequiredData: IPerfilUsuario = {
  id: 9734,
  nombreVisible: 'gosh loyally',
};

export const sampleWithPartialData: IPerfilUsuario = {
  id: 9060,
  nombreVisible: 'regarding',
  avatarUrl: 'unnaturally freight',
};

export const sampleWithFullData: IPerfilUsuario = {
  id: 4632,
  nombreVisible: 'labourer',
  bio: 'apropos cheerfully apropos',
  github: 'institute',
  webPersonal: 'surface aggravating',
  avatarUrl: 'apud',
};

export const sampleWithNewData: NewPerfilUsuario = {
  nombreVisible: 'unlike downshift drat',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
