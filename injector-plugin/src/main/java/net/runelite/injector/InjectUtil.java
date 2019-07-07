package net.runelite.injector;

import net.runelite.asm.ClassFile;
import net.runelite.asm.Field;
import net.runelite.asm.Method;
import net.runelite.asm.signature.Signature;
import net.runelite.deob.DeobAnnotations;

public class InjectUtil
{
	public static Method findStaticObMethod(Inject inject, String name) throws InjectionException
	{
		for (ClassFile c : inject.getVanilla().getClasses())
		{
			for (Method m : c.getMethods())
			{
				if (!m.getName().equals(name))
				{
					continue;
				}
				return m;
			}
		}

		throw new InjectionException(String.format("Method \"%s\" could not be found.", name));
	}

	public static Method findMethod(Inject inject, String name) throws InjectionException
	{
		return findMethod(inject, name, null);
	}

	public static Method findMethod(Inject inject, String name, String hint) throws InjectionException
	{
		if (hint != null)
		{
			ClassFile c = inject.getDeobfuscated().findClass(hint);

			if (c == null)
			{
				throw new InjectionException("Class " + hint + " doesn't exist. (check capitalization)");
			}

			Method deob = c.findMethod(name);

			if (deob != null)
			{
				String obfuscatedName = DeobAnnotations.getObfuscatedName(deob.getAnnotations());
				Signature obfuscatedSignature = DeobAnnotations.getObfuscatedSignature(deob);

				ClassFile ob = inject.toObClass(c);

				return ob.findMethod(obfuscatedName, (obfuscatedSignature != null) ? obfuscatedSignature : deob.getDescriptor());
			}
		}

		for (ClassFile c : inject.getDeobfuscated().getClasses())
		{
			for (Method m : c.getMethods())
			{
				if (!m.getName().equals(name))
				{
					continue;
				}

				String obfuscatedName = DeobAnnotations.getObfuscatedName(m.getAnnotations());
				Signature obfuscatedSignature = DeobAnnotations.getObfuscatedSignature(m);

				ClassFile c2 = inject.toObClass(c);

				return c2.findMethod(obfuscatedName, (obfuscatedSignature != null) ? obfuscatedSignature : m.getDescriptor());
			}
		}

		throw new InjectionException("Couldn't find method " + name);
	}

	public static Method findStaticMethod(Inject inject, String name) throws InjectionException
	{
		for (ClassFile c : inject.getDeobfuscated().getClasses())
		{
			for (Method m : c.getMethods())
			{
				if (!m.isStatic() || !m.getName().equals(name))
				{
					continue;
				}

				String obfuscatedName = DeobAnnotations.getObfuscatedName(m.getAnnotations());
				Signature obfuscatedSignature = DeobAnnotations.getObfuscatedSignature(m);

				ClassFile c2 = inject.toObClass(c);

				return c2.findMethod(obfuscatedName, (obfuscatedSignature != null) ? obfuscatedSignature : m.getDescriptor());
			}
		}

		throw new InjectionException("Couldn't find static method " + name);
	}


	public static Field findObField(Inject inject, String name) throws InjectionException
	{
		for (ClassFile c : inject.getVanilla().getClasses())
		{
			for (Field f : c.getFields())
			{
				if (!f.getName().equals(name))
				{
					continue;
				}
				return f;
			}
		}

		throw new InjectionException(String.format("Field \"%s\" could not be found.", name));
	}

	public static Field findDeobField(Inject inject, String name) throws InjectionException
	{
		return findDeobField(inject, name, null);
	}

	public static Field findDeobField(Inject inject, String name, String hint) throws InjectionException
	{
		if (hint != null)
		{
			ClassFile c = inject.getDeobfuscated().findClass(hint);
			if (c == null)
			{
				throw new InjectionException("Class " + hint + " doesn't exist. (check capitalization)");
			}

			for (Field f : c.getFields())
			{
				if (!f.getName().equals(name))
				{
					continue;
				}

				String obfuscatedName = DeobAnnotations.getObfuscatedName(f.getAnnotations());

				ClassFile c2 = inject.toObClass(c);
				return c2.findField(obfuscatedName);
			}
		}

		for (ClassFile c : inject.getDeobfuscated().getClasses())
		{
			for (Field f : c.getFields())
			{
				if (!f.getName().equals(name))
				{
					continue;
				}

				String obfuscatedName = DeobAnnotations.getObfuscatedName(f.getAnnotations());

				ClassFile c2 = inject.toObClass(c);
				return c2.findField(obfuscatedName);
			}
		}

		throw new InjectionException(String.format("Mapped field \"%s\" could not be found.", name));
	}

	public static Field findDeobFieldButUseless(Inject inject, String name) throws InjectionException
	{
		for (ClassFile c : inject.getDeobfuscated().getClasses())
		{
			for (Field f : c.getFields())
			{
				if (!f.getName().equals(name))
				{
					continue;
				}

				return f;
			}
		}

		throw new InjectionException(String.format("Mapped field \"%s\" could not be found.", name));
	}

	public static Method findStaticDeob(Inject inject, String name) throws InjectionException
	{
		for (ClassFile cf : inject.getDeobfuscated().getClasses())
		{
			if (cf.findMethod(name) != null)
			{
				return cf.findMethod(name);
			}
		}

		throw new InjectionException("Fycj you");
	}
}
