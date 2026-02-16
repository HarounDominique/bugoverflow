import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { CandidaturaService } from 'app/candidatura/candidatura.service';
import { CandidaturaDTO } from 'app/candidatura/candidatura.model';


@Component({
  selector: 'app-candidatura-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './candidatura-list.component.html'})
export class CandidaturaListComponent implements OnInit, OnDestroy {

  candidaturaService = inject(CandidaturaService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  candidaturas?: CandidaturaDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@candidatura.delete.success:Candidatura was removed successfully.`    };
    return messages[key];
  }

  ngOnInit() {
    this.loadData();
    this.navigationSubscription = this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.loadData();
      }
    });
  }

  ngOnDestroy() {
    this.navigationSubscription!.unsubscribe();
  }
  
  loadData() {
    this.candidaturaService.getAllCandidaturas()
        .subscribe({
          next: (data) => this.candidaturas = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (!confirm(this.getMessage('confirm'))) {
      return;
    }
    this.candidaturaService.deleteCandidatura(id)
        .subscribe({
          next: () => this.router.navigate(['/candidaturas'], {
            state: {
              msgInfo: this.getMessage('deleted')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

}
