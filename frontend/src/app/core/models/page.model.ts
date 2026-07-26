export interface PageInfo {
  size: number;
  totalElements: number;
  totalPages: number;
  number: number;
}

export interface HalPage<T> {
  _embedded?: Record<string, T[]>;
  page?: PageInfo;
}

export function itemsFromHal<T>(hal: HalPage<T>): T[] {
  if (!hal._embedded) {
    return [];
  }
  const firstKey = Object.keys(hal._embedded)[0];
  return firstKey ? hal._embedded[firstKey] : [];
}
