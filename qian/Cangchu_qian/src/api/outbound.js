import request from './request'

export const getOutboundOrders = () => request.get('/outbound')
export const getOutboundOrder = (id) => request.get(`/outbound/${id}`)
export const createOutboundOrder = (data) => request.post('/outbound', data)
export const confirmOutboundOrder = (id) => request.post(`/outbound/${id}/confirm`)
