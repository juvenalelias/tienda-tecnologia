package dominio;

import dominio.repositorio.RepositorioProducto;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import dominio.excepcion.GarantiaExtendidaException;
import dominio.repositorio.RepositorioGarantiaExtendida;

public class Vendedor {

    public static final String EL_PRODUCTO_TIENE_GARANTIA = "El producto ya cuenta con una garantia extendida";

    private RepositorioProducto repositorioProducto;
    private RepositorioGarantiaExtendida repositorioGarantia;

    public Vendedor(RepositorioProducto repositorioProducto, RepositorioGarantiaExtendida repositorioGarantia) {
        this.repositorioProducto = repositorioProducto;
        this.repositorioGarantia = repositorioGarantia;
    }

    public void generarGarantia(String codigo, String nombreCliente) {
    	
    	GarantiaExtendida garantia;
    	double precioExt = 0.0;
    	Date currentDateFin;
    	
    	Date currentDate = new Date();
    	LocalDateTime localDateTime = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    	currentDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    	
    	long vowels = codigo.toLowerCase().chars().mapToObj(i -> (char) i).filter(c -> "aeiouAEIOU"
    	.contains(String.valueOf(c))).count(); 
    	
    	Producto producto = repositorioProducto.obtenerPorCodigo(codigo);
		
    	if (producto == null){
			throw new GarantiaExtendidaException("El producto no existe en la base de datos");
		}
		else if (vowels > 2){
			throw new GarantiaExtendidaException("Este producto no cuenta con garantía extendida");
		} else if (tieneGarantia(codigo)){
    		throw new GarantiaExtendidaException(EL_PRODUCTO_TIENE_GARANTIA);
    	}
    	else{
    		if (producto.getPrecio() > 500000){
    			precioExt = ( producto.getPrecio() * 0.2 );
    	    	currentDateFin = Date.from(localDateTime.atZone(ZoneId.systemDefault()).plusDays(200).toInstant());
    	    	currentDateFin.equals(DayOfWeek.SUNDAY.plus(1));
//    	    	boolean isSunday = ld.getDayOfWeek().equals( DayOfWeek.SUNDAY ) ;
    			garantia = new GarantiaExtendida(producto, currentDate, currentDateFin, precioExt, nombreCliente);
    		} else {
    			precioExt = ( producto.getPrecio() * 0.1 );
    			currentDateFin = Date.from(localDateTime.atZone(ZoneId.systemDefault()).plusDays(100).toInstant());
    			currentDateFin.equals(DayOfWeek.SUNDAY.plus(1));
    			garantia = new GarantiaExtendida(producto, currentDate, currentDateFin, precioExt, nombreCliente);
    		}
    		
    		repositorioGarantia.agregar(garantia);
    	}    	
//        throw new UnsupportedOperationException("Método pendiente por implementar");		
    }

    public boolean tieneGarantia(String codigo) {
    	
    	Producto p = repositorioGarantia.obtenerProductoConGarantiaPorCodigo(codigo);
        return p != null ? true : false;
    }
}
