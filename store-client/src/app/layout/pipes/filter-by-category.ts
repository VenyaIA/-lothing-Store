import {Pipe, PipeTransform} from "@angular/core";
import {Product} from "../../models/Product";

@Pipe({
  name: 'filterByCategory'
})
export class FilterByCategory implements PipeTransform{
  transform(products: Product[], selectedCategoryIds: number[]): any[] {
    if (!products || !selectedCategoryIds || selectedCategoryIds.length === 0) {
      return products;
    }

    return products.filter(product => selectedCategoryIds.includes(product.category?.id!));
  }
}
