import request from './request'

export const getMaterials = () => request.get('/material')
export const createMaterial = (data) => request.post('/material', data)
export const updateMaterial = (id, data) => request.put(`/material/${id}`, data)
export const deleteMaterial = (id) => request.delete(`/material/${id}`)
