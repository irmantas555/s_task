import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {DialogComponent} from 'src/app/dialog/dialog.component';
import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DialogService {

  constructor(private dialog: MatDialog) { }

  showErrorDialog(message: string) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {message: message}
    this.dialog.open(DialogComponent, dialogConfig);
  }

}
