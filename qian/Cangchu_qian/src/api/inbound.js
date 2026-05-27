import request from './request'

export const getInboundOrders = () => request.get('/inbound')
export const getInboundOrder = (id) => request.get(`/inbound/${id}`)
export const createInboundOrder = (data) => request.post('/inbound', data)
export const confirmInboundOrder = (id) => request.post(`/inbound/${id}/confirm`)
