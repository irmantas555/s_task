export class Currency{
  id: number;
  code: string;
  name: string;
  rate: number

  constructor(id: number, code: string, name: string, rate: number) {
    this.id = id;
    this.code = code;
    this.name = name;
    this.rate = rate;
  }
}
