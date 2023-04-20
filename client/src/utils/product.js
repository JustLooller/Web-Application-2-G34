export class ProductDTO {
    constructor(ean, brand, model, description, image) {
        this.ean = ean;
        this.brand = brand;
        this.model = model;
        this.description = description;
        this.image = image;
    }

    static empty() {
        return new ProductDTO('', '', '', '', '');
    }

    static fromJson(json) {
        if (json instanceof String) {
            json = JSON.parse(json);
        }
        return new ProductDTO(json.ean, json.brand.name, json.model, json.description, json.image);
    }

    static async getAllProducts(){
        const URL = '/api/products/';
        try {
            const response = await fetch(URL);
            const json = await response.json();
            return json.map(ProductDTO.fromJson)
        } catch (error) {
            console.error(error);
        }
    }
    static async getProductDetails(ean) {
        const URL = '/api/products/' + ean;
        try {
            const response = await fetch(URL);
            const json = await response.json();
            return ProductDTO.fromJson(json);
        } catch (error) {
            console.log(error);
        }
    }

}