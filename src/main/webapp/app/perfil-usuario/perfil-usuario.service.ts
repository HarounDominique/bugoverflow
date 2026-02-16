import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { PerfilUsuarioDTO } from 'app/perfil-usuario/perfil-usuario.model';
import { map } from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';


@Injectable({
  providedIn: 'root',
})
export class PerfilUsuarioService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/perfilUsuarios';

  getAllPerfilUsuarios() {
    return this.http.get<PerfilUsuarioDTO[]>(this.resourcePath);
  }

  getPerfilUsuario(id: number) {
    return this.http.get<PerfilUsuarioDTO>(this.resourcePath + '/' + id);
  }

  createPerfilUsuario(perfilUsuarioDTO: PerfilUsuarioDTO) {
    return this.http.post<number>(this.resourcePath, perfilUsuarioDTO);
  }

  updatePerfilUsuario(id: number, perfilUsuarioDTO: PerfilUsuarioDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, perfilUsuarioDTO);
  }

  deletePerfilUsuario(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

  getUsuarioValues() {
    return this.http.get<Record<string, string>>(this.resourcePath + '/usuarioValues')
        .pipe(map(transformRecordToMap));
  }

  getSkillsValues() {
    return this.http.get<Record<string, string>>(this.resourcePath + '/skillsValues')
        .pipe(map(transformRecordToMap));
  }

}
