import { Component } from '@angular/core';
import { BackendDataService } from 'src/app/services/backend-data.service';

@Component({
  selector: 'app-transaction-history',
  templateUrl: './transaction-history.component.html',
  styleUrls: ['./transaction-history.component.css']
})
export class TransactionHistoryComponent{

  constructor(private backendService: BackendDataService) { }

}
