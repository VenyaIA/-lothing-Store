import {Pipe, PipeTransform} from "@angular/core";
import {Product} from "../../models/Product";

@Pipe({
  name: 'searchProduct',
})
export class SearchPipe implements PipeTransform {
  transform(products: Product[], search = ''): Product[] {
    if (!search.trim()) {
      return products
    }

    return products.filter(product => {
      return product.title.toLowerCase().includes(search.toLowerCase())
    })
  }

}
