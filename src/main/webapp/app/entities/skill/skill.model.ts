import { IPerfilUsuario } from 'app/entities/perfil-usuario/perfil-usuario.model';

export interface ISkill {
  id: number;
  nombre?: string | null;
  descripcion?: string | null;
  perfilUsuarios?: IPerfilUsuario[] | null;
}

export type NewSkill = Omit<ISkill, 'id'> & { id: null };
