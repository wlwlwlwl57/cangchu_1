import request from './request'

export const getWarehouses = () => request.get('/warehouse')
export const createWarehouse = (data) => request.post('/warehouse', data)
export const updateWarehouse = (id, data) => request.put(`/warehouse/${id}`, data)
