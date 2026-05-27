import request from './request'

export const getInventoryByMaterial = (materialCode) => request.get(`/inventory/material/${materialCode}`)
export const searchInventory = (keyword) => request.get('/inventory/search', { params: { keyword } })
export const getInventoryByCategory = () => request.get('/inventory/category')
