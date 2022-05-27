import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {NavbarComponent} from './navbar/navbar.component';
import {ExchageComponent} from './exchage/exchage.component';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientModule} from '@angular/common/http';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatOptionModule} from '@angular/material/core';
import {MatSelectModule} from '@angular/material/select';
import {MatButtonModule} from '@angular/material/button';
import {MatInputModule} from '@angular/material/input';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {CurrencyListComponent} from './currency-list/currency-list.component';
import {RouterModule, Routes} from '@angular/router';
import {MatTableModule} from '@angular/material/table';
import {HistoricalDataComponent} from './historical-data/historical-data.component';
import {DialogComponent} from './dialog/dialog.component';
import {MatDialogModule} from '@angular/material/dialog';

const appRoutes: Routes = [
  {path: '', redirectTo: '/exchange', pathMatch: 'full'},
  {path: 'exchange', component: ExchageComponent},
  {path: 'currency-list', component: CurrencyListComponent},
  {path: 'historical', component: HistoricalDataComponent},
  {path: 'currency-list/:date', component: CurrencyListComponent},
];

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    ExchageComponent,
    CurrencyListComponent,
    HistoricalDataComponent,
    DialogComponent,
  ],
  imports: [
    RouterModule.forRoot(appRoutes),
    BrowserModule,
    NoopAnimationsModule,
    HttpClientModule,
    NoopAnimationsModule,
    MatFormFieldModule,
    MatSelectModule,
    MatOptionModule,
    MatIconModule,
    MatButtonModule,
    MatInputModule,
    FormsModule,
    ReactiveFormsModule,
    MatTableModule,
    MatDialogModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
