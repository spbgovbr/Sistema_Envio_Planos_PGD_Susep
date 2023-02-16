package br.gov.economia.apipgdsusep.validator;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@SuppressWarnings("rawtypes")
@Component
@RequestScoped
public class ValidadorCPFCNPJ implements Validator {

	private static List<String> cpfInvalidos = Arrays.asList(new String []{
			"00000000000","11111111111","22222222222","33333333333","44444444444","55555555555","66666666666","77777777777","88888888888","99999999999"});
	private static List<String> cnpjInvalidos = Arrays.asList(new String []{
			"00000000000000","11111111111111","22222222222222","33333333333333","44444444444444","55555555555555","66666666666666","77777777777777","88888888888888","99999999999999"});

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		String cpfCnpj = value.toString();

		if (cpfCnpj.length() == 14) {
			cpfCnpj = cpfCnpj.replace(".", "").replace("-", "");
			if(cpfCnpj.equals("___________"))
				cpfCnpj = null;
		} else if (cpfCnpj.length() == 18) {
			cpfCnpj = cpfCnpj.replace(".", "").replace("/", "").replace("-", "");
			if(cpfCnpj.equals("______________"))
				cpfCnpj = null;
		}

		if (cpfCnpj == null || !isCPFOuCNPJValido(cpfCnpj)) 
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "CPF/CNPJ Inválido!", ""));
	}

	public static boolean isCPFOuCNPJValido(String cpfOuCnpj) {
		cpfOuCnpj = getCpfOuCnpjSemFormatacao(cpfOuCnpj);

		//------- Rotina para CPF
		if (cpfOuCnpj.length() == 11) {
			if(cpfInvalidos.contains(cpfOuCnpj)) 
				return false;

			String cpf1 = cpfOuCnpj.substring(0, 9); // rcpf1
			String cpf2 = cpfOuCnpj.substring(9); // rcpf2
			int i;
			int d1 = 0;
			for (i = 0; i < 9; i++) {
				d1 += Integer.parseInt(cpf1.substring(i, i + 1)) * (10 - i);
			}
			d1 = 11 - (d1 % 11);
			if (d1 > 9) {
				d1 = 0;
			}
			if (Integer.parseInt(cpf2.substring(0, 1)) != d1) {
				return false;
			}
			d1 *= 2;
			for (i = 0; i < 9; i++) {
				d1 += Integer.parseInt(cpf1.substring(i, i + 1)) * (11 - i);
			}
			d1 = 11 - (d1 % 11);
			if (d1 > 9) {
				d1 = 0;
			}
			if (Integer.parseInt(cpf2.substring(1, 2)) != d1) {
				return false;
			}
			return true;
		}
		//-------- Rotina para CNPJ         
		else if (cpfOuCnpj.length() == 14) {
			if(cnpjInvalidos.contains(cpfOuCnpj)) return false;

			char charsCNPJ[] = cpfOuCnpj.toCharArray();
			Integer digito1 = calculoPrimeiroDigitoVerificador(charsCNPJ);
			Integer digito2 = calculoSegundoDigitoVerificador(charsCNPJ, digito1);

			String digitoIdentificadorCalculado = digito1.toString() + digito2.toString();
			String digitoIdentificadorInformado = charsCNPJ[charsCNPJ.length - 2] + "" + charsCNPJ[charsCNPJ.length - 1];

			return digitoIdentificadorCalculado.equals(digitoIdentificadorInformado);

		} else
			return false;
	}
	
	public static String getCpfOuCnpjComFormatacao(String cpfOuCnpj) {
		StringBuilder cpfOuCnpjAux = new StringBuilder();
		if (cpfOuCnpj.length() == 11) {
			cpfOuCnpjAux.append(cpfOuCnpj.substring(0, 3) + ".");
			cpfOuCnpjAux.append(cpfOuCnpj.substring(3, 6) + ".");
			cpfOuCnpjAux.append(cpfOuCnpj.substring(6, 9) + "-");
			cpfOuCnpjAux.append(cpfOuCnpj.substring(9, 11));
		} else if (cpfOuCnpj.length() == 14) {
			cpfOuCnpjAux.append(cpfOuCnpj.substring(0, 2) + ".");
			cpfOuCnpjAux.append(cpfOuCnpj.substring(2, 5) + ".");
			cpfOuCnpjAux.append(cpfOuCnpj.substring(5, 8) + "/");
			cpfOuCnpjAux.append(cpfOuCnpj.substring(8, 12) + "-");
			cpfOuCnpjAux.append(cpfOuCnpj.substring(12, 14));
		}
		return cpfOuCnpjAux.toString();
	}
	
	public static String getCpfOuCnpjSemFormatacao(String cpfOuCnpj) {
		if (cpfOuCnpj.length() == 14) {
			cpfOuCnpj = cpfOuCnpj.replace(".", "").replace("-", "");
		} else if (cpfOuCnpj.length() == 18) {
			cpfOuCnpj = cpfOuCnpj.replace(".", "").replace("/", "").replace("-", "");
		}
		return cpfOuCnpj;
	}
	
	public static boolean isCPF(String documento) {
		return ((documento != null && StringUtils.isNotBlank(documento)) && 
				(documento.length() >= 11 && documento.length() <= 14) ? true : false);
	}
	
	public static boolean isCNPJ(String documento) {
		return (documento != null && StringUtils.isNotBlank(documento) && 
				documento.length() >= 14 && documento.length() <= 18 ? true : false);
	}
	
	private static int calculoPrimeiroDigitoVerificador(char[] charsCNPJ) {
		int fator = 5;
		int soma = 0;
		for (int i = 0; i < charsCNPJ.length - 2; i++) {
			char c = charsCNPJ[i];
			soma += Integer.parseInt(String.valueOf(c)) * fator;
			if (i == 3){
				fator = 9;
			}else{
				fator--;
			}
		}

		int valor = (soma) % 11;

		int digito1 = 11 - valor;
		if ((soma) % 11 < 2) {
			digito1 = 0;
		}
		return digito1;
	}

	private static int calculoSegundoDigitoVerificador(char[] charsCNPJ, int digito1) {
		int fator2 = 6;
		int soma2 = 0;
		for (int i = 0; i < charsCNPJ.length - 2; i++) {
			char c = charsCNPJ[i];
			soma2 += Integer.parseInt(String.valueOf(c)) * fator2;
			if (i == 4){
				fator2 = 9;
			}else{
				fator2--;
			}
		}
		soma2 += digito1 * 2;
		int valor2 = (soma2) % 11;

		int digito2 = 11 - valor2;
		if ((soma2) % 11 < 2)
			digito2 = 0;
		return digito2;
	}
	
}