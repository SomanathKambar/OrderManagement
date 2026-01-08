import React from "react";
import { clsx, type ClassValue } from "clsx";
import { twMerge } from "tailwind-merge";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export const Button = React.forwardRef<HTMLButtonElement, React.ButtonHTMLAttributes<HTMLButtonElement> & { variant?: 'primary' | 'secondary' | 'outline' | 'ghost' | 'destructive' }>(
  ({ className, variant = 'primary', ...props }, ref) => {
    const variants = {
      primary: "bg-blue-600 text-white hover:bg-blue-700 shadow-sm",
      secondary: "bg-slate-100 text-slate-900 hover:bg-slate-200",
      outline: "border border-slate-200 bg-transparent hover:bg-slate-50 text-slate-700",
      ghost: "hover:bg-slate-100 text-slate-700",
      destructive: "bg-red-600 text-white hover:bg-red-700 shadow-sm",
    };
    return (
      <button
        ref={ref}
        className={cn(
          "inline-flex items-center justify-center rounded-md text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-slate-950 disabled:pointer-events-none disabled:opacity-50 h-9 px-4 py-2",
          variants[variant],
          className
        )}
        {...props}
      />
    );
  }
);
Button.displayName = "Button";

export const Card = ({ className, children }: { className?: string, children: React.ReactNode }) => (
  <div className={cn("rounded-xl border border-slate-200 bg-white text-slate-950 shadow-sm", className)}>
    {children}
  </div>
);

export const Badge = ({ className, variant = 'default', children }: { className?: string, variant?: 'default' | 'success' | 'warning' | 'destructive' | 'outline', children: React.ReactNode }) => {
    const variants = {
        default: "border-transparent bg-slate-900 text-slate-50",
        success: "border-transparent bg-green-500 text-white",
        warning: "border-transparent bg-yellow-500 text-white",
        destructive: "border-transparent bg-red-500 text-white",
        outline: "text-slate-950 border-slate-200",
    };
    return (
        <div className={cn("inline-flex items-center rounded-full border px-2.5 py-0.5 text-xs font-semibold transition-colors focus:outline-none focus:ring-2 focus:ring-slate-950 focus:ring-offset-2", variants[variant], className)}>
            {children}
        </div>
    );
}
