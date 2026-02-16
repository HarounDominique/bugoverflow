import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { UsuarioPreferenciaDTO } from 'app/usuario-preferencia/usuario-preferencia.model';
import { map } from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';


@Injectable({
  providedIn: 'root',
})
export class UsuarioPreferenciaService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/usuarioPreferencias';

  getAllUsuarioPreferencias() {
    return this.http.get<UsuarioPreferenciaDTO[]>(this.resourcePath);
  }

  getUsuarioPreferencia(id: number) {
    return this.http.get<UsuarioPreferenciaDTO>(this.resourcePath + '/' + id);
  }

  createUsuarioPreferencia(usuarioPreferenciaDTO: UsuarioPreferenciaDTO) {
    return this.http.post<number>(this.resourcePath, usuarioPreferenciaDTO);
  }

  updateUsuarioPreferencia(id: number, usuarioPreferenciaDTO: UsuarioPreferenciaDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, usuarioPreferenciaDTO);
  }

  deleteUsuarioPreferencia(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

  getUsuarioValues() {
    return this.http.get<Record<string, string>>(this.resourcePath + '/usuarioValues')
        .pipe(map(transformRecordToMap));
  }

}
