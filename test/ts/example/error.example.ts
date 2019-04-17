interface ProductionOrder {
    production_order_id: string,
    something?: number
}

declare class UsesProductionOrder {
    static production_order(productionOrder: ProductionOrder): void;
}

declare class Connection {
    executeQuery(query : string) : any;
}

declare class Runtime {
    static con(): Connection;
}

var con : Connection = Runtime.con();
con.executeQuery("query");
con.close();
UsesProductionOrder.production_order({});

does_not_exist.production_order({});
