package test.core.data.mapper

interface NetworkMapper<Network, Model> {
    fun mapFromNetwork(network: Network): Model
}