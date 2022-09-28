package com.unosoft.ecomercialapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.unosoft.ecomercialapp.db.cotizacion.EntityEditQuotationDetail
import com.unosoft.ecomercialapp.db.cotizacion.EntityQuotationMaster
import com.unosoft.ecomercialapp.db.pedido.EntityEditPedidoDetail
import com.unosoft.ecomercialapp.entity.TableBasic.MonedaResponse

@Dao
interface DAO {

    //*************   QUERY DE TABLAS    *********************
    @Query("SELECT * FROM EntityCondicionPago")
    fun getAllCondicionPago(): List<EntityCondicionPago>

    @Query("SELECT * FROM EntityDepartamento")
    fun getAllDepartamento(): List<EntityDepartamento>

    @Query("SELECT * FROM EntityDistrito")
    fun getAllDistrito(): List<EntityDistrito>

    @Query("SELECT * FROM EntityDocIdentidad")
    fun getAllDocIdentidad(): List<EntityDocIdentidad>

    @Query("SELECT * FROM EntityFrecuenciaDias")
    fun getAllFrecuenciaDias(): List<EntityFrecuenciaDias>

    @Query("SELECT * FROM EntityMoneda")
    fun getAllMoneda(): List<EntityMoneda>

    @Query("SELECT * FROM EntityProvincia")
    fun getAllProvincia(): List<EntityProvincia>

    @Query("SELECT * FROM EntityUnidadMedida")
    fun getAllUnidadMedida(): List<EntityUnidadMedida>

    @Query("SELECT * FROM EntityPedidoMaster")
    fun getAllPedidoMaster(): List<EntityPedidoMaster>

    @Query("SELECT * FROM EntityListaPrecio")
    fun getAllListaPrecio(): List<EntityListaPrecio>

    @Query("SELECT * FROM EntityListProct")
    fun getAllListProct(): List<EntityListProct>

    @Query("SELECT * FROM EntityVendedor")
    fun getAllVendedor(): List<EntityVendedor>

    @Query("SELECT * FROM EntityDataCabezera")
    fun getAllDataCabezera(): List<EntityDataCabezera>

    @Query("SELECT * FROM EntityDataLogin")
    fun getAllDataLogin(): List<EntityDataLogin>

    @Query("SELECT * FROM EntityEmpresa")
    fun getAllEmpresa(): List<EntityEmpresa>


    //*************   INSERT DE TABLAS    *********************
    @Insert
    fun insertCondicionPago( insertCondicionPago: EntityCondicionPago)

    @Insert
    fun insertDepartamento( insertDepartamento: EntityDepartamento)

    @Insert
    fun insertDistrito( insertDistrito: EntityDistrito)

    @Insert
    fun insertDocIdentidad( insertDocIdentidad: EntityDocIdentidad)

    @Insert
    fun insertFrecuenciaDias( insertFrecuenciaDias: EntityFrecuenciaDias)

    @Insert
    fun insertMoneda( insertMoneda: EntityMoneda)

    @Insert
    fun insertProvincia( insertProvincia: EntityProvincia)

    @Insert
    fun insertUnidadMedida( insertUnidadMedida: EntityUnidadMedida)

    @Insert
    fun insertListaPrecio( insertListaPrecio: EntityListaPrecio)

    @Insert
    fun insertListProct( insertListProctCot: EntityListProct)

    @Insert
    fun insertVendedor( insertVendedor: EntityVendedor)

    @Insert
    fun insertDataCabezera( insertDataCabezera: EntityDataCabezera)

    @Insert
    fun insertDataLogin( insertDataLogin: EntityDataLogin)

    @Insert
    fun insertEmpresa( insertEmpresa: EntityEmpresa)



    //*************   Datos    *********************
    @Query("SELECT Nombre,Numero,Referencia1  FROM EntityMoneda WHERE id= :id")
    fun getTipoMoneda(id:Int): MonedaResponse

    @Query("SELECT COUNT(*) from EntityMoneda")
    fun getSizeMoneda(): Int


    @Query("SELECT COUNT(*) from EntityListProct")
    fun getSizeListProctCot(): Int





    //*********  ELIMINAR DATOS DE LAS TABLAS  **************
    @Query("DELETE FROM EntityCondicionPago")
    fun deleteTableCondicionPago()

    @Query("DELETE FROM EntityDepartamento")
    fun deleteTableDepartamento()

    @Query("DELETE FROM EntityDistrito")
    fun deleteTableDistrito()

    @Query("DELETE FROM EntityDocIdentidad")
    fun deleteTableDocIdentidad()

    @Query("DELETE FROM EntityFrecuenciaDias")
    fun deleteTableFrecuenciaDias()

    @Query("DELETE FROM EntityMoneda")
    fun deleteTableMoneda()

    @Query("DELETE FROM EntityProvincia")
    fun deleteTableProvincia()

    @Query("DELETE FROM EntityUnidadMedida")
    fun deleteTableUnidadMedida()

    @Query("DELETE FROM EntityPedidoMaster")
    fun deleteTablePedidoMaster()

    @Query("DELETE FROM EntityQuotationMaster")
    fun deleteTableQuotationMaster()

    @Query("DELETE FROM EntityListaPrecio")
    fun deleteTableListaPrecio()

    @Query("DELETE FROM EntityListProct")
    fun deleteTableListProct()

    @Query("DELETE FROM EntityVendedor")
    fun deleteTableVendedor()

    @Query("DELETE FROM EntityDataCabezera")
    fun deleteTableDataCabezera()

    @Query("DELETE FROM EntityDataLogin")
    fun deleteTableDataLogin()

    @Query("DELETE FROM EntityEmpresa")
    fun deleteTableEmpresa()

    @Query("DELETE FROM EntityEmpresa WHERE id=:id")
    fun deleteTableEmpresaID(id: Int)



    //*********  REINICIAR LOS ID AUTOGENERADOS   **************

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'EntityCondicionPago'")
    fun clearPrimaryKeyCondicionPago()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'EntityDepartamento'")
    fun clearPrimaryKeyDepartamento()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'EntityDistrito'")
    fun clearPrimaryKeyDistrito()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'EntityDocIdentidad'")
    fun clearPrimaryKeyDocIdentidad()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'EntityFrecuenciaDias'")
    fun clearPrimaryKeyFrecuenciaDias()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'EntityMoneda'")
    fun clearPrimaryKeyMoneda()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'EntityProvincia'")
    fun clearPrimaryKeyProvincia()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'EntityUnidadMedida'")
    fun clearPrimaryKeyUnidadMedida()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'EntityPedidoMaster'")
    fun clearPrimaryKeyPedidoMaster()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'EntityQuotationMaster'")
    fun clearPrimaryKeyQuotationMaster()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'EntityListaPrecio'")
    fun clearPrimaryKeyListaPrecio()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'EntityListProct'")
    fun clearPrimaryKeyListProct()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'EntityVendedor'")
    fun clearPrimaryKeyVendedor()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'EntityDataCabezera'")
    fun clearPrimaryKeyDataCabezera()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'EntityDataLogin'")
    fun clearPrimaryKeyDataLogin()

    @Query("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'EntityEmpresa'")
    fun clearPrimaryKeyEmpresa()


    //*****************  CONSULTA DE LA EXISTENCIA DE DATABASE *******************
    @Query("SELECT EXISTS(SELECT * FROM EntityCondicionPago)")
    fun isExists(): Boolean

    //*****************  CONSULTA DE LA EXISTENCIA DE DATABASE *******************
    @Query("SELECT EXISTS(SELECT * FROM EntityListProct)")
    fun isExistsEntityListProct(): Boolean

    //*****************  CONSULTA DE LA EXISTENCIA DE DATABASE *******************
    @Query("SELECT EXISTS(SELECT * FROM EntityEmpresa)")
    fun isExistsEntityEmpresa(): Boolean




    //*****************  CONSULTA TABLAS BASICAS *******************
    @Query("SELECT Nombre FROM EntityCondicionPago WHERE Codigo = :Numero  ")
    fun findnamecategoriapagowithnumero(Numero :String): String



//*****************  Gestion de Pedidos Cruds *******************//
    //--Inserts
    @Insert
    fun insertPedidoMaster( insertPedidoMaster: EntityPedidoMaster)
    @Insert
    fun insertEditPedidoDetail( insertEditPedidoDetail: EntityEditPedidoDetail)

    //--Gets
    @Query("SELECT * FROM EntityEditPedidoDetail")
    fun getAllDetail(): List<EntityEditPedidoDetail>

    //--Exists
    @Query("SELECT EXISTS(SELECT * FROM EntityEditPedidoDetail)")
    fun isExistsEntityListEditPedido(): Boolean

    //--Deletes
    @Query("DELETE FROM EntityEditPedidoDetail")
    fun deleteTableEntityEditPedidoDetail()



    //*****************  Gestion de Cotizacion Cruds *******************//
    //--Inserts
    @Insert
    fun insertQuotationMaster( insertQuotationMaster: EntityQuotationMaster)
    @Insert
    fun insertEditQuotatiDetail( insertEditQuotationDetail: EntityEditQuotationDetail)

    //--Gets
    @Query("SELECT * FROM EntityQuotationMaster")
    fun getAllQuotationMaster(): List<EntityQuotationMaster>

    @Query("SELECT * FROM EntityEditQuotationDetail")
    fun getAllQuotationDetail(): List<EntityEditQuotationDetail>

    //--Exists
    @Query("SELECT EXISTS(SELECT * FROM EntityEditQuotationDetail)")
    fun isExistsEntityListEditQuotation(): Boolean

    //--Deletes
    @Query("DELETE FROM EntityEditQuotationDetail")
    fun deleteTableEntityEditQuotationDetail()


    //--Exists
    @Query("SELECT EXISTS(SELECT * FROM EntityListProct)")
    fun isExistsEntityProductList(): Boolean


    //*****************  CABEZERA COTIZACION *******************//
    //--Inserts
    @Query("SELECT EXISTS(SELECT * FROM EntityDataCabezera)")
    fun isExistsEntityDataCabezera(): Boolean
}
