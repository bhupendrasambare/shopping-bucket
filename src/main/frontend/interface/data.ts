export interface Item {
    id: number;
    itemName: string;
    itemQty: number;
    createdDate?: string;
    updateDate?: string;
}

export interface PaginationData<T> {
    data: T[];
    page: number;
    total: number;
    size: number;
}

export interface ApiResponse<T> {
    code: string;
    status: string;
    message: string;
    data: PaginationData<T>;
}
  
export interface SaleDetail {
    id: number;
    email: string;
    phone: string;
    customerName: string;
    address: string;
    state: string;
    minor: boolean | null; // Optional, if minor is nullable in your system
    shopDate: string; // LocalDateTime as ISO string
    items: Item; // Associated Item details
    dateOfBirth: string; // Date of Birth as ISO string
    quantity: number;
    price: number;
    payAmount: number;
}