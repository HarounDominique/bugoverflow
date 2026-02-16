import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { UsuarioSkillDTO } from 'app/usuario-skill/usuario-skill.model';
import { map } from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';


@Injectable({
  providedIn: 'root',
})
export class UsuarioSkillService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/usuarioSkills';

  getAllUsuarioSkills() {
    return this.http.get<UsuarioSkillDTO[]>(this.resourcePath);
  }

  getUsuarioSkill(id: number) {
    return this.http.get<UsuarioSkillDTO>(this.resourcePath + '/' + id);
  }

  createUsuarioSkill(usuarioSkillDTO: UsuarioSkillDTO) {
    return this.http.post<number>(this.resourcePath, usuarioSkillDTO);
  }

  updateUsuarioSkill(id: number, usuarioSkillDTO: UsuarioSkillDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, usuarioSkillDTO);
  }

  deleteUsuarioSkill(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

  getUsuarioValues() {
    return this.http.get<Record<string, string>>(this.resourcePath + '/usuarioValues')
        .pipe(map(transformRecordToMap));
  }

  getSkillValues() {
    return this.http.get<Record<string, string>>(this.resourcePath + '/skillValues')
        .pipe(map(transformRecordToMap));
  }

}
